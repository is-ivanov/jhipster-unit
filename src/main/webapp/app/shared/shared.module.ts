import { NgModule } from '@angular/core';

import { SharedLibsModule } from './shared-libs.module';
import { FindLanguageFromKeyPipe } from './language/find-language-from-key.pipe';
import { TranslateDirective } from './language/translate.directive';
import { AlertComponent } from './alert/alert.component';
import { AlertErrorComponent } from './alert/alert-error.component';
import { HasAnyAuthorityDirective } from './auth/has-any-authority.directive';
import { DurationPipe } from './date/duration.pipe';
import { FormatMediumDatetimePipe } from './date/format-medium-datetime.pipe';
import { FormatMediumDatePipe } from './date/format-medium-date.pipe';
import { SortByDirective } from './sort/sort-by.directive';
import { SortDirective } from './sort/sort.directive';
import { ItemCountComponent } from './pagination/item-count.component';
import { ProjectsComponent } from './selectors/projects/projects.component';
import { StatusesLinesComponent } from './selectors/statuses-lines/statuses-lines.component';
import { BlocksComponent } from './selectors/blocks/blocks.component';
import { ClearFilterComponent } from './buttons/clear-filter/clear-filter.component';
import { RevisionsComponent } from './selectors/revisions/revisions.component';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';

@NgModule({
	imports: [SharedLibsModule, NgMultiSelectDropDownModule],
	declarations: [
		FindLanguageFromKeyPipe,
		TranslateDirective,
		AlertComponent,
		AlertErrorComponent,
		HasAnyAuthorityDirective,
		DurationPipe,
		FormatMediumDatetimePipe,
		FormatMediumDatePipe,
		SortByDirective,
		SortDirective,
		ItemCountComponent,
		ProjectsComponent,
		StatusesLinesComponent,
		BlocksComponent,
		ClearFilterComponent,
		RevisionsComponent
	],
	exports: [
		SharedLibsModule,
		FindLanguageFromKeyPipe,
		TranslateDirective,
		AlertComponent,
		AlertErrorComponent,
		HasAnyAuthorityDirective,
		DurationPipe,
		FormatMediumDatetimePipe,
		FormatMediumDatePipe,
		SortByDirective,
		SortDirective,
		ItemCountComponent,
		ProjectsComponent,
		StatusesLinesComponent,
		BlocksComponent,
		ClearFilterComponent,
		RevisionsComponent
	]
})
export class SharedModule {
}
