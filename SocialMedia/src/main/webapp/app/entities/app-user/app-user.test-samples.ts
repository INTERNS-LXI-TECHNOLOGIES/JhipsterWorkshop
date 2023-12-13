import { IAppUser, NewAppUser } from './app-user.model';

export const sampleWithRequiredData: IAppUser = {
  id: 13077,
};

export const sampleWithPartialData: IAppUser = {
  id: 28262,
};

export const sampleWithFullData: IAppUser = {
  id: 9993,
  phone: '4977921437',
};

export const sampleWithNewData: NewAppUser = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
