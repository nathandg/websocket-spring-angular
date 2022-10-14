import { Component, OnDestroy, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { PADOLabsChatMessage } from '../models/padolabsMessage';
import { SocketService } from '../services/socket.service';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit, OnDestroy {

  protected socketService: SocketService;

  constructor(socketService: SocketService) {
    this.socketService = socketService;
  }

  ngOnInit(): void {
    this.socketService.openConnection();
  }

  ngOnDestroy(): void {
    throw new Error('Method not implemented.');
  }

  public sendMessage(sendForm: NgForm)
  {
    const padolabsMessage = new PADOLabsChatMessage(sendForm.value.user, sendForm.value.message);
    
    this.socketService.sendMessage(padolabsMessage);
    sendForm.controls['message'].reset();
  }

}
