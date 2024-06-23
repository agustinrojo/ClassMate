import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'capitalize'
})
export class CapitalizePipe implements PipeTransform {

  transform(value: string, ...args: unknown[]): string {
    const splitString = value
      .split(' ')
      .map((s) => `${s[0].toUpperCase()}${s.slice(1)}`);
    return splitString.join(' ');
  }

}
