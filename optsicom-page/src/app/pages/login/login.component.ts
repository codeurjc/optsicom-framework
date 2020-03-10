import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  public form: FormGroup;
  public hide: boolean = true;
  public errorLogin: boolean = false;

  constructor(private router: Router,
    private authenticationService: AuthenticationService) { }

  ngOnInit(): void {
    this.form = new FormGroup({
      email: new FormControl("", Validators.required),
      pass: new FormControl("", Validators.required)
    });
  }

  public login() {
    let userName = this.form.get("email").value;
    let pass = this.form.get("pass").value;

    this.authenticationService.login(userName, pass).subscribe(
      () => {
        this.router.navigate(['']);
      },
      () => {
        this.errorLogin = true;

        setTimeout(() => {
          this.errorLogin = false;
        }, 9000);
      }
    )
  }

}
