import { IUser } from 'app/entities/user/user.model';
import { IPost } from 'app/entities/post/post.model';

export interface IAppUser {
  id: number;
  phone?: string | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
  posts?: IPost[] | null;
}

export type NewAppUser = Omit<IAppUser, 'id'> & { id: null };
