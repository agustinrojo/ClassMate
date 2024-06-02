import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-email-sent',
  templateUrl: './email-sent.component.html',
  styleUrl: './email-sent.component.css'
})
export class EmailSentComponent implements OnInit{

  public email: string;

  constructor(private _route: ActivatedRoute){
    this.email ="";
  }

  ngOnInit(): void {
    this._route.params.subscribe(params => {
      const email = params["email"];
      this.email = email;
    })
  }

}
