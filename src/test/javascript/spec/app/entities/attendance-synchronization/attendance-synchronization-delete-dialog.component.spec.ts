import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CodeNodeErpTestModule } from '../../../test.module';
import { MockEventManager } from '../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';
import { AttendanceSynchronizationDeleteDialogComponent } from 'app/entities/attendance-synchronization/attendance-synchronization-delete-dialog.component';
import { AttendanceSynchronizationService } from 'app/entities/attendance-synchronization/attendance-synchronization.service';

describe('Component Tests', () => {
  describe('AttendanceSynchronization Management Delete Component', () => {
    let comp: AttendanceSynchronizationDeleteDialogComponent;
    let fixture: ComponentFixture<AttendanceSynchronizationDeleteDialogComponent>;
    let service: AttendanceSynchronizationService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CodeNodeErpTestModule],
        declarations: [AttendanceSynchronizationDeleteDialogComponent],
      })
        .overrideTemplate(AttendanceSynchronizationDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AttendanceSynchronizationDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AttendanceSynchronizationService);
      mockEventManager = TestBed.get(JhiEventManager);
      mockActiveModal = TestBed.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.closeSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
      });
    });
  });
});
