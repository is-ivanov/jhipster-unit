import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'line',
        data: { pageTitle: 'unitApp.line.home.title' },
        loadChildren: () => import('./line/line.module').then(m => m.LineModule),
      },
      {
        path: 'priority-punch',
        data: { pageTitle: 'unitApp.priorityPunch.home.title' },
        loadChildren: () => import('./priority-punch/priority-punch.module').then(m => m.PriorityPunchModule),
      },
      {
        path: 'type-punch',
        data: { pageTitle: 'unitApp.typePunch.home.title' },
        loadChildren: () => import('./type-punch/type-punch.module').then(m => m.TypePunchModule),
      },
      {
        path: 'block',
        data: { pageTitle: 'unitApp.block.home.title' },
        loadChildren: () => import('./block/block.module').then(m => m.BlockModule),
      },
      {
        path: 'project',
        data: { pageTitle: 'unitApp.project.home.title' },
        loadChildren: () => import('./project/project.module').then(m => m.ProjectModule),
      },
      {
        path: 'punch-list',
        data: { pageTitle: 'unitApp.punchList.home.title' },
        loadChildren: () => import('./punch-list/punch-list.module').then(m => m.PunchListModule),
      },
      {
        path: 'comment-punch',
        data: { pageTitle: 'unitApp.commentPunch.home.title' },
        loadChildren: () => import('./comment-punch/comment-punch.module').then(m => m.CommentPunchModule),
      },
      {
        path: 'company',
        data: { pageTitle: 'unitApp.company.home.title' },
        loadChildren: () => import('./company/company.module').then(m => m.CompanyModule),
      },
      {
        path: 'punch-item',
        data: { pageTitle: 'unitApp.punchItem.home.title' },
        loadChildren: () => import('./punch-item/punch-item.module').then(m => m.PunchItemModule),
      },
      {
        path: 'app-user',
        data: { pageTitle: 'unitApp.appUser.home.title' },
        loadChildren: () => import('./app-user/app-user.module').then(m => m.AppUserModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
