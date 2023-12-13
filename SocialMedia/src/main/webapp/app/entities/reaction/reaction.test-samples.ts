import dayjs from 'dayjs/esm';

import { IReaction, NewReaction } from './reaction.model';

export const sampleWithRequiredData: IReaction = {
  id: 1492,
};

export const sampleWithPartialData: IReaction = {
  id: 21030,
  reactionMode: 'LIKE',
};

export const sampleWithFullData: IReaction = {
  id: 26559,
  time: dayjs('2023-12-12T10:52'),
  reactionMode: 'LIKE',
};

export const sampleWithNewData: NewReaction = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
