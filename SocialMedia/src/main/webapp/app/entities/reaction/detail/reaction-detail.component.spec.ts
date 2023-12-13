import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ReactionDetailComponent } from './reaction-detail.component';

describe('Reaction Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReactionDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ReactionDetailComponent,
              resolve: { reaction: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ReactionDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load reaction on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ReactionDetailComponent);

      // THEN
      expect(instance.reaction).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
