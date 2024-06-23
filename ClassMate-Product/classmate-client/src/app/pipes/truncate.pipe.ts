import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'truncate'
})
export class TruncatePipe implements PipeTransform {

  transform(value: string, limit: number = 20, completeWords: boolean = false, ellipsis: string = '...'): string {
    if (!value) return '';

    if (value.length <= limit) return value;

    let truncatedValue = value.substring(0, limit);
    if (completeWords) {
      const lastSpace = truncatedValue.lastIndexOf(' ');
      if (lastSpace > 0) {
        truncatedValue = truncatedValue.substring(0, lastSpace);
      }
    }

    return truncatedValue + ellipsis;
  }

}
