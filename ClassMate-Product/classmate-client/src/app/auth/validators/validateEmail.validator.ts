import { Form, FormControl, ValidatorFn } from "@angular/forms";

export let validateEmail = (control: FormControl) : { [key: string]: any } | null => {
  const email: string = control.value.trim();
  const emailRegex = /^\d{1,6}@(sistemas|industrial|mecanica|quimica|electrica|electronica)\.frc\.utn\.edu\.ar$/;

  if (!emailRegex.test(email)) {
    return { 'invalidEmailFormat': true };
  }

  return null;
}
