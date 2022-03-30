import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PriorityPunchService } from '../service/priority-punch.service';

import { PriorityPunchComponent } from './priority-punch.component';

describe('PriorityPunch Management Component', () => {
  let comp: PriorityPunchComponent;
  let fixture: ComponentFixture<PriorityPunchComponent>;
  let service: PriorityPunchService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PriorityPunchComponent],
    })
      .overrideTemplate(PriorityPunchComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PriorityPunchComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PriorityPunchService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.priorityPunches?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
