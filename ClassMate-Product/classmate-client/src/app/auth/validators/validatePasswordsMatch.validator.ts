import { FormGroup } from "@angular/forms"

export let validatePasswordsMatch = (control: FormGroup) : { [key: string]: any } | null => {
  const password = control.get("password")?.value;
  const rePassword = control.get("rePassword")?.value;

  if(password !== rePassword){
    return {
      'passwordDoesntMatch': true
    }
  }
  return null
}
