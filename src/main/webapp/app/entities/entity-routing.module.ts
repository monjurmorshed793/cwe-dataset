import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'cwe-list',
        data: { pageTitle: 'CweLists' },
        loadChildren: () => import('./cwe-list/cwe-list.module').then(m => m.CweListModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
