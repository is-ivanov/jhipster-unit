import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CommentPunchDetailComponent } from './comment-punch-detail.component';

describe('CommentPunch Management Detail Component', () => {
  let comp: CommentPunchDetailComponent;
  let fixture: ComponentFixture<CommentPunchDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CommentPunchDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ commentPunch: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CommentPunchDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CommentPunchDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load commentPunch on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.commentPunch).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
