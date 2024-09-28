import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'linkify'
})
export class LinkifyPipe implements PipeTransform {


  transform(value?: string): string | undefined {
    if (!value) {
      return value;
    }

    // Pattern to match URLs
    const urlPattern = /(\b(https?|ftp|file):\/\/[-A-Z0-9+&@#\/%?=~_|!:,.;]*[-A-Z0-9+&@#\/%=~_|])/ig;
    value = value.replace(urlPattern, '<a href="$1" target="_blank" style="text-decoration: underline;">$1</a>');

    // Pattern to match p/nickname:id
    const tagPattern = /p\/([a-zA-Z][^}\s]*):(\d+)/g;
    value = value.replace(tagPattern, (match, nickname, userId) => {
      return `<a href="/profile/${userId}" class="tagged-user">p/${nickname}</a>`;
    });

    const forumTagPattern = /f\/([a-zA-Z0-9_\-\s]+):(\d+)/g;
    value = value.replace(forumTagPattern, (match, forumName, forumId) => {
      return `<a href="/forum/${forumId}" class="tagged-forum">f/${forumName}</a>`;
    });

    return value;
  }
}
