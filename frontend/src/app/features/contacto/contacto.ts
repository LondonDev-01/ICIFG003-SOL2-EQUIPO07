import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-contacto',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './contacto.html',
  styleUrls: ['./contacto.css']
})
export class Contacto {
  formData = {
    nombre: '',
    email: '',
    asunto: '',
    mensaje: ''
  };

  mensajeEnviado = false;

  enviarFormulario() {
    if (this.formData.nombre && this.formData.email && this.formData.asunto && this.formData.mensaje) {
      console.log('Formulario enviado:', this.formData);
      this.mensajeEnviado = true;
      
      // Resetear formulario después de 3 segundos
      setTimeout(() => {
        this.formData = { nombre: '', email: '', asunto: '', mensaje: '' };
        this.mensajeEnviado = false;
      }, 3000);
    }
  }
}
