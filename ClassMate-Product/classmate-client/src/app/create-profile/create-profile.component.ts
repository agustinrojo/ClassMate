import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-create-profile',
  templateUrl: './create-profile.component.html',
  styleUrl: './create-profile.component.css'
})
export class CreateProfileComponent implements OnInit{
  public createProfileForm!: FormGroup;

  constructor(
    private _fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.createProfileForm = this._fb.group({
      username: ["", Validators.required, []],
      profilePhoto: ["", Validators.required, []],
      description: ["", Validators.required, []]
    })
  }

}
