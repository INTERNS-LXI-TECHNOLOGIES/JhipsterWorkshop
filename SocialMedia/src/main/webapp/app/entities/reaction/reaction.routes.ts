import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ReactionComponent } from './list/reaction.component';
import { ReactionDetailComponent } from './detail/reaction-detail.component';
import { ReactionUpdateComponent } from './update/reaction-update.component';
import ReactionResolve from './route/reaction-routing-resolve.service';

const reactionRoute: Routes = [
  {
    path: '',
    component: ReactionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReactionDetailComponent,
    resolve: {
      reaction: ReactionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReactionUpdateComponent,
    resolve: {
      reaction: ReactionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReactionUpdateComponent,
    resolve: {
      reaction: ReactionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default reactionRoute;
