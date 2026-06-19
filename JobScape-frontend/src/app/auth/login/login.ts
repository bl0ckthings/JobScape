import { Component, inject } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { Register } from '../register/register';
import { AuthService } from '../../core/services/auth.service';
import { LoginRequest } from '../../shared/models/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [RouterLink, ReactiveFormsModule, NgOptimizedImage],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  private formBuilder: FormBuilder = inject(FormBuilder);
  private router = inject(Router);
  private authService = inject(AuthService);
  loading = false;
  error: string | null = null;

  form: FormGroup = this.formBuilder.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
  });



  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.loading = true;
    this.error = null;
    this.authService
      .login(this.form.value)
      .subscribe({
        next: () => this.router.navigateByUrl('/dashboard'),
        error:(err) => {
          this.error = err?.error?.message || "Problème lors de la connexion";
          this.loading = false;
        }
      });
  }
}
