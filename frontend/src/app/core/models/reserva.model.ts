export interface Reserva {
  id: number;
  fechaReserva: string;
  observacion: string;
  fechaCreacion: string;
  idEstudiante: number;
  nombreEstudiante: string;
  idSala: number;
  nombreSala: string;
  idHorario: number;
  bloqueHorario?: string;
  idEstado: number;
  nombreEstado: string;
}