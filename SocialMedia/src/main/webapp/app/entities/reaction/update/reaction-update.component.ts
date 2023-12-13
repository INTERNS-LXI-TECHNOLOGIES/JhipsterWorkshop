import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';
import { IPost } from 'app/entities/post/post.model';
import { PostService } from 'app/entities/post/service/post.service';
import { ReactionMode } from 'app/entities/enumerations/reaction-mode.model';
import { ReactionService } from '../service/reaction.service';
import { IReaction } from '../reaction.model';
import { ReactionFormService, ReactionFormGroup } from './reaction-form.service';

@Component({
  standalone: true,
  selector: 'jhi-reaction-update',
  templateUrl: './reaction-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ReactionUpdateComponent implements OnInit {
  isSaving = false;
  reaction: IReaction | null = null;
  reactionModeValues = Object.keys(ReactionMode);

  appUsersSharedCollection: IAppUser[] = [];
  postsSharedCollection: IPost[] = [];

  editForm: ReactionFormGroup = this.reactionFormService.createReactionFormGroup();

  constructor(
    protected reactionService: ReactionService,
    protected reactionFormService: ReactionFormService,
    protected appUserService: AppUserService,
    protected postService: PostService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareAppUser = (o1: IAppUser | null, o2: IAppUser | null): boolean => this.appUserService.compareAppUser(o1, o2);

  comparePost = (o1: IPost | null, o2: IPost | null): boolean => this.postService.comparePost(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reaction }) => {
      this.reaction = reaction;
      if (reaction) {
        this.updateForm(reaction);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reaction = this.reactionFormService.getReaction(this.editForm);
    if (reaction.id !== null) {
      this.subscribeToSaveResponse(this.reactionService.update(reaction));
    } else {
      this.subscribeToSaveResponse(this.reactionService.create(reaction));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReaction>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(reaction: IReaction): void {
    this.reaction = reaction;
    this.reactionFormService.resetForm(this.editForm, reaction);

    this.appUsersSharedCollection = this.appUserService.addAppUserToCollectionIfMissing<IAppUser>(
      this.appUsersSharedCollection,
      reaction.reactedBy,
    );
    this.postsSharedCollection = this.postService.addPostToCollectionIfMissing<IPost>(this.postsSharedCollection, reaction.post);
  }

  protected loadRelationshipsOptions(): void {
    this.appUserService
      .query()
      .pipe(map((res: HttpResponse<IAppUser[]>) => res.body ?? []))
      .pipe(
        map((appUsers: IAppUser[]) => this.appUserService.addAppUserToCollectionIfMissing<IAppUser>(appUsers, this.reaction?.reactedBy)),
      )
      .subscribe((appUsers: IAppUser[]) => (this.appUsersSharedCollection = appUsers));

    this.postService
      .query()
      .pipe(map((res: HttpResponse<IPost[]>) => res.body ?? []))
      .pipe(map((posts: IPost[]) => this.postService.addPostToCollectionIfMissing<IPost>(posts, this.reaction?.post)))
      .subscribe((posts: IPost[]) => (this.postsSharedCollection = posts));
  }
}
