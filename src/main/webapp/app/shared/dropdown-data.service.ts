import { EventEmitter, Injectable } from '@angular/core';
import { IBlock } from '../entities/block/block.model';

@Injectable({
	providedIn: 'root',
})
export class DropdownDataService {
	projectNotifier: EventEmitter<number> = new EventEmitter();
	blockNotifier: EventEmitter<number> = new EventEmitter();
	blocksNotifier: EventEmitter<IBlock[]> = new EventEmitter();
	statusLineNotifier: EventEmitter<string> = new EventEmitter();

	notifyProjectChange(projectId: number | undefined): void {
		if (projectId) {
			this.projectNotifier.emit(projectId);
		}
	}

	notifyBlockChange(blockId: number | undefined): void {
		if (blockId) {
			this.blockNotifier.emit(blockId);
		}
	}

	notifyProjectChangeBlocks(blocks: IBlock[] | undefined): void {
		if (blocks && blocks.length > 0) {
			this.blocksNotifier.emit(blocks);
		}
	}

	notifyStatusLineChange(statusLine: string | undefined): void {
		if (statusLine) {
			this.statusLineNotifier.emit(statusLine);
		}
	}
}
