import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-contacto',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './contacto.html',
  styleUrls: ['./contacto.css']
})
export class Contacto {
  private readonly apiUrl = 'http://localhost:8080/api/contacto';
  private timeoutMensajeExito?: ReturnType<typeof setTimeout>;

  formData = {
    nombre: '',
    email: '',
    asunto: '',
    mensaje: ''
  };

  mensajeEnviado = false;

  constructor(private http: HttpClient) {}

  enviarFormulario() {
    const nombre = this.formData.nombre.trim();
    const email = this.formData.email.trim();
    const asunto = this.formData.asunto.trim();
    const mensaje = this.formData.mensaje.trim();

    if (!nombre || !email || !asunto || !mensaje) {
      return;
    }

    this.http.post(this.apiUrl, { nombre, email, asunto, mensaje }).subscribe({
      next: () => {
      this.mensajeEnviado = true;

        if (this.timeoutMensajeExito) {
          clearTimeout(this.timeoutMensajeExito);
        }

        this.timeoutMensajeExito = setTimeout(() => {
          this.mensajeEnviado = false;
          this.formData = { nombre: '', email: '', asunto: '', mensaje: '' };
        }, 5000);
      }
    });
  }
}
