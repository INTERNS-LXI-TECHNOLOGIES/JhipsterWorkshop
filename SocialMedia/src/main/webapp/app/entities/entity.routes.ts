import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'app-user',
    data: { pageTitle: 'socialMediaApp.appUser.home.title' },
    loadChildren: () => import('./app-user/app-user.routes'),
  },
  {
    path: 'post',
    data: { pageTitle: 'socialMediaApp.post.home.title' },
    loadChildren: () => import('./post/post.routes'),
  },
  {
    path: 'reaction',
    data: { pageTitle: 'socialMediaApp.reaction.home.title' },
    loadChildren: () => import('./reaction/reaction.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
