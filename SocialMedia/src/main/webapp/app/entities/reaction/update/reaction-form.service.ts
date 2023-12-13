import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IReaction, NewReaction } from '../reaction.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReaction for edit and NewReactionFormGroupInput for create.
 */
type ReactionFormGroupInput = IReaction | PartialWithRequiredKeyOf<NewReaction>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IReaction | NewReaction> = Omit<T, 'time'> & {
  time?: string | null;
};

type ReactionFormRawValue = FormValueOf<IReaction>;

type NewReactionFormRawValue = FormValueOf<NewReaction>;

type ReactionFormDefaults = Pick<NewReaction, 'id' | 'time'>;

type ReactionFormGroupContent = {
  id: FormControl<ReactionFormRawValue['id'] | NewReaction['id']>;
  time: FormControl<ReactionFormRawValue['time']>;
  reactionMode: FormControl<ReactionFormRawValue['reactionMode']>;
  reactedBy: FormControl<ReactionFormRawValue['reactedBy']>;
  post: FormControl<ReactionFormRawValue['post']>;
};

export type ReactionFormGroup = FormGroup<ReactionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReactionFormService {
  createReactionFormGroup(reaction: ReactionFormGroupInput = { id: null }): ReactionFormGroup {
    const reactionRawValue = this.convertReactionToReactionRawValue({
      ...this.getFormDefaults(),
      ...reaction,
    });
    return new FormGroup<ReactionFormGroupContent>({
      id: new FormControl(
        { value: reactionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      time: new FormControl(reactionRawValue.time),
      reactionMode: new FormControl(reactionRawValue.reactionMode),
      reactedBy: new FormControl(reactionRawValue.reactedBy),
      post: new FormControl(reactionRawValue.post),
    });
  }

  getReaction(form: ReactionFormGroup): IReaction | NewReaction {
    return this.convertReactionRawValueToReaction(form.getRawValue() as ReactionFormRawValue | NewReactionFormRawValue);
  }

  resetForm(form: ReactionFormGroup, reaction: ReactionFormGroupInput): void {
    const reactionRawValue = this.convertReactionToReactionRawValue({ ...this.getFormDefaults(), ...reaction });
    form.reset(
      {
        ...reactionRawValue,
        id: { value: reactionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ReactionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      time: currentTime,
    };
  }

  private convertReactionRawValueToReaction(rawReaction: ReactionFormRawValue | NewReactionFormRawValue): IReaction | NewReaction {
    return {
      ...rawReaction,
      time: dayjs(rawReaction.time, DATE_TIME_FORMAT),
    };
  }

  private convertReactionToReactionRawValue(
    reaction: IReaction | (Partial<NewReaction> & ReactionFormDefaults),
  ): ReactionFormRawValue | PartialWithRequiredKeyOf<NewReactionFormRawValue> {
    return {
      ...reaction,
      time: reaction.time ? reaction.time.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
