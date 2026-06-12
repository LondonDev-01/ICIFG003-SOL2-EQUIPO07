import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-ayuda',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './ayuda.html',
  styleUrls: ['./ayuda.css']
})
export class Ayuda {
  faqs = [
    {
      pregunta: '¿Cómo puedo reservar una sala?',
      respuesta: 'Para reservar una sala, dirígete a la sección "Salas", selecciona la sala deseada, elige la fecha y hora, y confirma tu reserva. Recibirás una confirmación por correo electrónico.',
      abierto: false
    },
    {
      pregunta: '¿Puedo cancelar mi reserva?',
      respuesta: 'Sí, puedes cancelar tu reserva hasta 24 horas antes de la fecha programada. Para hacerlo, accede a tu perfil y busca la opción de cancelación en tu reserva activa.',
      abierto: false
    },
    {
      pregunta: '¿Cuál es el horario de funcionamiento de las salas?',
      respuesta: 'Las salas de estudio están disponibles de lunes a domingo, de 7:00 AM a 10:00 PM. Durante días festivos, pueden haber cambios en el horario.',
      abierto: false
    },
    {
      pregunta: '¿Cuántas personas pueden usar una sala?',
      respuesta: 'La capacidad varía según el tipo de sala. Las salas pequeñas pueden alojar 2-4 personas, las medianas 5-8 personas, y las grandes hasta 15 personas. Consulta los detalles al seleccionar la sala.',
      abierto: false
    },
    {
      pregunta: '¿Hay costo para usar las salas?',
      respuesta: 'El uso de las salas es completamente gratuito para los estudiantes y personal de la universidad. Solo necesitas estar registrado en el sistema.',
      abierto: false
    },
    {
      pregunta: '¿Cómo puedo reportar un problema técnico?',
      respuesta: 'Si encuentras algún problema técnico con la plataforma, contáctanos a través de la sección "Contacto" o envía un correo a soporte@biblioteca.edu. Nuestro equipo responderá en 24 horas.',
      abierto: false
    }
  ];

  pasos = [
    {
      numero: 1,
      titulo: 'Accede a tu cuenta',
      descripcion: 'Inicia sesión en el sistema con tus credenciales de la universidad.'
    },
    {
      numero: 2,
      titulo: 'Selecciona una sala',
      descripcion: 'Ve a la sección "Salas" y elige la que mejor se adapte a tus necesidades.'
    },
    {
      numero: 3,
      titulo: 'Elige fecha y hora',
      descripcion: 'Selecciona la fecha y horario en el que deseas usar la sala.'
    },
    {
      numero: 4,
      titulo: 'Confirma tu reserva',
      descripcion: 'Revisa los detalles y confirma. Recibirás una confirmación por correo.'
    },
    {
      numero: 5,
      titulo: 'Presenta tu código',
      descripcion: 'Presenta el código de confirmación en la recepción de la biblioteca.'
    }
  ];

  toggleFaq(index: number) {
    this.faqs[index].abierto = !this.faqs[index].abierto;
  }
}
