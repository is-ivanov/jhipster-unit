import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BlockDetailComponent } from './block-detail.component';

describe('Block Management Detail Component', () => {
  let comp: BlockDetailComponent;
  let fixture: ComponentFixture<BlockDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BlockDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ block: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BlockDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BlockDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load block on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.block).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
