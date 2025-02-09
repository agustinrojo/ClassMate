import { Component, Input } from '@angular/core';
import { DeleteRequestDTOResponse } from '../../../services/dto/delete-request/delete-request-response.dto';

@Component({
  selector: 'app-report-message',
  templateUrl: './report-message.component.html',
  styleUrl: './report-message.component.css'
})
export class ReportMessageComponent {
  @Input() public deleteRequests!: DeleteRequestDTOResponse[];
}
