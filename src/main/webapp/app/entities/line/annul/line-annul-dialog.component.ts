import { Component } from '@angular/core';
import { ILine, Line } from '../line.model';
import { LineService } from '../service/line.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { StatusLine } from '../../enumerations/status-line.model';

@Component({
  selector: 'jhi-line-annul-dialog',
  templateUrl: './line-annul-dialog.component.html'
})
export class LineAnnulDialogComponent {
	line?: ILine;

	constructor(protected lineService: LineService, protected activeModal: NgbActiveModal) {}

	cancel(): void {
		this.activeModal.dismiss();
	}

	confirmAnnul(id: number): void {
		const line = new Line(id);
		line.status = StatusLine.DELETED;
		this.lineService.partialUpdate(line).subscribe(() => {
			this.activeModal.close('annulled')
		});
	}
}
