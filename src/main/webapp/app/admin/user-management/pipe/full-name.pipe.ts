import { Pipe, PipeTransform } from '@angular/core';
import { IUser } from '../../../entities/user/user.model';

@Pipe({
  name: 'fullName'
})
export class FullNamePipe implements PipeTransform {

  transform(user: IUser  | null | undefined): string {
    return `${user?.firstName?.charAt(0) ?? ''}.${user?.lastName ?? ''}`;
  }

}
