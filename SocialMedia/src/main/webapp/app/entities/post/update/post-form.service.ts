import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPost, NewPost } from '../post.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPost for edit and NewPostFormGroupInput for create.
 */
type PostFormGroupInput = IPost | PartialWithRequiredKeyOf<NewPost>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPost | NewPost> = Omit<T, 'createdAt'> & {
  createdAt?: string | null;
};

type PostFormRawValue = FormValueOf<IPost>;

type NewPostFormRawValue = FormValueOf<NewPost>;

type PostFormDefaults = Pick<NewPost, 'id' | 'createdAt'>;

type PostFormGroupContent = {
  id: FormControl<PostFormRawValue['id'] | NewPost['id']>;
  createdAt: FormControl<PostFormRawValue['createdAt']>;
  content: FormControl<PostFormRawValue['content']>;
  hashTags: FormControl<PostFormRawValue['hashTags']>;
  appUser: FormControl<PostFormRawValue['appUser']>;
};

export type PostFormGroup = FormGroup<PostFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PostFormService {
  createPostFormGroup(post: PostFormGroupInput = { id: null }): PostFormGroup {
    const postRawValue = this.convertPostToPostRawValue({
      ...this.getFormDefaults(),
      ...post,
    });
    return new FormGroup<PostFormGroupContent>({
      id: new FormControl(
        { value: postRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      createdAt: new FormControl(postRawValue.createdAt),
      content: new FormControl(postRawValue.content, {
        validators: [Validators.maxLength(1024)],
      }),
      hashTags: new FormControl(postRawValue.hashTags),
      appUser: new FormControl(postRawValue.appUser),
    });
  }

  getPost(form: PostFormGroup): IPost | NewPost {
    return this.convertPostRawValueToPost(form.getRawValue() as PostFormRawValue | NewPostFormRawValue);
  }

  resetForm(form: PostFormGroup, post: PostFormGroupInput): void {
    const postRawValue = this.convertPostToPostRawValue({ ...this.getFormDefaults(), ...post });
    form.reset(
      {
        ...postRawValue,
        id: { value: postRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PostFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
    };
  }

  private convertPostRawValueToPost(rawPost: PostFormRawValue | NewPostFormRawValue): IPost | NewPost {
    return {
      ...rawPost,
      createdAt: dayjs(rawPost.createdAt, DATE_TIME_FORMAT),
    };
  }

  private convertPostToPostRawValue(
    post: IPost | (Partial<NewPost> & PostFormDefaults),
  ): PostFormRawValue | PartialWithRequiredKeyOf<NewPostFormRawValue> {
    return {
      ...post,
      createdAt: post.createdAt ? post.createdAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
