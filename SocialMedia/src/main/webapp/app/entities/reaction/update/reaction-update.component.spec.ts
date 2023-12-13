import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';
import { IPost } from 'app/entities/post/post.model';
import { PostService } from 'app/entities/post/service/post.service';
import { IReaction } from '../reaction.model';
import { ReactionService } from '../service/reaction.service';
import { ReactionFormService } from './reaction-form.service';

import { ReactionUpdateComponent } from './reaction-update.component';

describe('Reaction Management Update Component', () => {
  let comp: ReactionUpdateComponent;
  let fixture: ComponentFixture<ReactionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reactionFormService: ReactionFormService;
  let reactionService: ReactionService;
  let appUserService: AppUserService;
  let postService: PostService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ReactionUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ReactionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReactionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reactionFormService = TestBed.inject(ReactionFormService);
    reactionService = TestBed.inject(ReactionService);
    appUserService = TestBed.inject(AppUserService);
    postService = TestBed.inject(PostService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call AppUser query and add missing value', () => {
      const reaction: IReaction = { id: 456 };
      const reactedBy: IAppUser = { id: 5994 };
      reaction.reactedBy = reactedBy;

      const appUserCollection: IAppUser[] = [{ id: 12757 }];
      jest.spyOn(appUserService, 'query').mockReturnValue(of(new HttpResponse({ body: appUserCollection })));
      const additionalAppUsers = [reactedBy];
      const expectedCollection: IAppUser[] = [...additionalAppUsers, ...appUserCollection];
      jest.spyOn(appUserService, 'addAppUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reaction });
      comp.ngOnInit();

      expect(appUserService.query).toHaveBeenCalled();
      expect(appUserService.addAppUserToCollectionIfMissing).toHaveBeenCalledWith(
        appUserCollection,
        ...additionalAppUsers.map(expect.objectContaining),
      );
      expect(comp.appUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Post query and add missing value', () => {
      const reaction: IReaction = { id: 456 };
      const post: IPost = { id: 23186 };
      reaction.post = post;

      const postCollection: IPost[] = [{ id: 547 }];
      jest.spyOn(postService, 'query').mockReturnValue(of(new HttpResponse({ body: postCollection })));
      const additionalPosts = [post];
      const expectedCollection: IPost[] = [...additionalPosts, ...postCollection];
      jest.spyOn(postService, 'addPostToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reaction });
      comp.ngOnInit();

      expect(postService.query).toHaveBeenCalled();
      expect(postService.addPostToCollectionIfMissing).toHaveBeenCalledWith(
        postCollection,
        ...additionalPosts.map(expect.objectContaining),
      );
      expect(comp.postsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const reaction: IReaction = { id: 456 };
      const reactedBy: IAppUser = { id: 27022 };
      reaction.reactedBy = reactedBy;
      const post: IPost = { id: 30441 };
      reaction.post = post;

      activatedRoute.data = of({ reaction });
      comp.ngOnInit();

      expect(comp.appUsersSharedCollection).toContain(reactedBy);
      expect(comp.postsSharedCollection).toContain(post);
      expect(comp.reaction).toEqual(reaction);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReaction>>();
      const reaction = { id: 123 };
      jest.spyOn(reactionFormService, 'getReaction').mockReturnValue(reaction);
      jest.spyOn(reactionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reaction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reaction }));
      saveSubject.complete();

      // THEN
      expect(reactionFormService.getReaction).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(reactionService.update).toHaveBeenCalledWith(expect.objectContaining(reaction));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReaction>>();
      const reaction = { id: 123 };
      jest.spyOn(reactionFormService, 'getReaction').mockReturnValue({ id: null });
      jest.spyOn(reactionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reaction: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reaction }));
      saveSubject.complete();

      // THEN
      expect(reactionFormService.getReaction).toHaveBeenCalled();
      expect(reactionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReaction>>();
      const reaction = { id: 123 };
      jest.spyOn(reactionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reaction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reactionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAppUser', () => {
      it('Should forward to appUserService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(appUserService, 'compareAppUser');
        comp.compareAppUser(entity, entity2);
        expect(appUserService.compareAppUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePost', () => {
      it('Should forward to postService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(postService, 'comparePost');
        comp.comparePost(entity, entity2);
        expect(postService.comparePost).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
