import dayjs from 'dayjs/esm';

import { IPost, NewPost } from './post.model';

export const sampleWithRequiredData: IPost = {
  id: 26488,
};

export const sampleWithPartialData: IPost = {
  id: 22192,
};

export const sampleWithFullData: IPost = {
  id: 1943,
  createdAt: dayjs('2023-12-13T05:51'),
  content: 'readily',
  hashTags: 'excepting or',
};

export const sampleWithNewData: NewPost = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
