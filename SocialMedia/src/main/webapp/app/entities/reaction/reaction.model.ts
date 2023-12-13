import dayjs from 'dayjs/esm';
import { IAppUser } from 'app/entities/app-user/app-user.model';
import { IPost } from 'app/entities/post/post.model';
import { ReactionMode } from 'app/entities/enumerations/reaction-mode.model';

export interface IReaction {
  id: number;
  time?: dayjs.Dayjs | null;
  reactionMode?: keyof typeof ReactionMode | null;
  reactedBy?: IAppUser | null;
  post?: IPost | null;
}

export type NewReaction = Omit<IReaction, 'id'> & { id: null };
