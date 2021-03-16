import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CweList } from '../cwe-list.model';
import { DataUtils } from 'app/core/util/data-util.service';

import { CweListDetailComponent } from './cwe-list-detail.component';

describe('Component Tests', () => {
  describe('CweList Management Detail Component', () => {
    let comp: CweListDetailComponent;
    let fixture: ComponentFixture<CweListDetailComponent>;
    let dataUtils: DataUtils;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CweListDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ cweList: new CweList(123) }) },
          },
        ],
      })
        .overrideTemplate(CweListDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CweListDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = TestBed.inject(DataUtils);
    });

    describe('OnInit', () => {
      it('Should load cweList on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.cweList).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeBase64, fakeContentType);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeBase64, fakeContentType);
      });
    });
  });
});
