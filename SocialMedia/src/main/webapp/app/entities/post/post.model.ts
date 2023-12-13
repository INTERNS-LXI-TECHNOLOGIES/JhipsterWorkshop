import dayjs from 'dayjs/esm';
import { IReaction } from 'app/entities/reaction/reaction.model';
import { IAppUser } from 'app/entities/app-user/app-user.model';

export interface IPost {
  id: number;
  createdAt?: dayjs.Dayjs | null;
  content?: string | null;
  hashTags?: string | null;
  likes?: IReaction[] | null;
  appUser?: IAppUser | null;
}

export type NewPost = Omit<IPost, 'id'> & { id: null };
