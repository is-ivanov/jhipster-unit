import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TypePunchService } from '../service/type-punch.service';

import { TypePunchComponent } from './type-punch.component';

describe('TypePunch Management Component', () => {
  let comp: TypePunchComponent;
  let fixture: ComponentFixture<TypePunchComponent>;
  let service: TypePunchService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TypePunchComponent],
    })
      .overrideTemplate(TypePunchComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypePunchComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TypePunchService);

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
    expect(comp.typePunches?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
