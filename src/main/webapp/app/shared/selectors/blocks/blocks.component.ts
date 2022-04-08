import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { IBlock } from '../../../entities/block/block.model';
import { BlockService } from '../../../entities/block/service/block.service';
import { map } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';

@Component({
	selector: 'jhi-blocks',
	templateUrl: './blocks.component.html',
})
export class BlocksComponent implements OnInit {
	blocks?: IBlock[] = [];
	@Input() selectedBlockId?: number;
	@Output() updateBlockInFilter = new EventEmitter<IBlock>();

	constructor(protected blockService: BlockService) {}

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

	private loadBlocks(): void {
		this.blockService
			.query({
				eagerload: false,
				sort: ['number,asc'],
			})
			.pipe(map((res: HttpResponse<IBlock[]>) => res.body ?? []))
			.subscribe((loadedBlocks: IBlock[]) => (this.blocks = loadedBlocks));
	}
}
