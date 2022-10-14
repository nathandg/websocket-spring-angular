import { Injectable } from '@angular/core';
import { PADOLabsChatMessage } from '../models/padolabsMessage';


@Injectable({
  providedIn: 'root'
})

export class SocketService {

  public padolabsMessages: PADOLabsChatMessage[] = [] ;

  socket: WebSocket | undefined;

  constructor( ){ }


  public openConnection(){
    this.socket = new WebSocket('ws://localhost:7000/chat/nathan');

    this.socket.onopen = (event) => {
      console.log(event);
    }

    this.socket.onmessage = (event) => {
      console.log(event);
      this.padolabsMessages.push(JSON.parse(event.data));
    }

    this.socket.onclose = (event) => {
      console.log(event);
    }

  }

  public sendMessage(padolabsMessage: PADOLabsChatMessage){

    console.log("not use stringify");
    console.log(padolabsMessage);
    console.log("use stringify");
    console.log(JSON.stringify(padolabsMessage));

    this.socket?.send(JSON.stringify(padolabsMessage));
  }

  public closeConnection(){
    this.socket?.close();
  }

}
