import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { IBlock } from '../../../entities/block/block.model';
import { BlockService } from '../../../entities/block/service/block.service';
import { map } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { DropdownDataService } from '../../dropdown-data.service';

@Component({
	selector: 'jhi-blocks',
	templateUrl: './blocks.component.html',
})
export class BlocksComponent implements OnInit, OnDestroy {
	blocks?: IBlock[] = [];
	@Input() selectedBlockId?: number;
	@Output() updateBlockInFilter = new EventEmitter<IBlock>();
	selectedProjectId?: number;
	notifierSubscription: Subscription =
		this.dropdownDataService.projectNotifier.subscribe(projectId => {
			this.selectedProjectId = projectId;
			this.loadBlocks();
		});

	constructor(protected blockService: BlockService,
	            protected dropdownDataService: DropdownDataService) {}

	ngOnInit(): void {
		this.loadBlocks();
	}

	updateFilter(): void {
		this.updateBlockInFilter.emit({
			id: this.selectedBlockId,
		});
	}

	trackBlockById(index: number, item: IBlock): number {
		return item.id!;
	}

	ngOnDestroy(): void {
		this.notifierSubscription.unsubscribe();
	}

	private loadBlocks(): void {
		const req = {
			eagerload: false,
			sort: ['number,asc'],
		};
		if (this.selectedProjectId) {
			Object.assign(req, { 'projectId.equals': this.selectedProjectId });
		}

		this.blockService
			.query(req)
			.pipe(map((res: HttpResponse<IBlock[]>) => res.body ?? []))
			.subscribe((loadedBlocks: IBlock[]) => (this.blocks = loadedBlocks));
	}
}
