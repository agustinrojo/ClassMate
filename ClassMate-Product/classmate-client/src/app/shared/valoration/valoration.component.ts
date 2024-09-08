import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Valoration } from '../../home/interfaces/valoration.interface';
import { Subject, Subscription, debounceTime, delay, of } from 'rxjs';
import { ValorationType } from './enum/valoration.enum';

@Component({
  selector: 'app-valoration',
  templateUrl: './valoration.component.html',
  styleUrl: './valoration.component.css'
})
export class ValorationComponent implements OnInit{
  private likeAction = new Subject<ValorationType>();
  private likeSubscription: Subscription | undefined;
  @Input() public valoration!: Valoration;
  @Output() public likeEvent: EventEmitter<void> = new EventEmitter();
  @Output() public dislikeEvent: EventEmitter<void> = new EventEmitter();
  @Output() public removeEvent: EventEmitter<void> = new EventEmitter();

  ngOnInit(): void {

    this.likeSubscription = this.likeAction.pipe(
      debounceTime(150) // Ajusta el tiempo de debounce según tus necesidades (en milisegundos)
    ).subscribe((action) => {
      this.emitValorationEvent(action);
    });


    if(!this.valoration.valoration){
      this.valoration.valoration = 0;
    }
  }

  public dislike() {
    if(this.valoration.likedByUser){
      this.valoration.likedByUser = false;
      this.valoration.dislikedByUser = true;
      this.valoration.valoration -= 2;
      this.likeAction.next(ValorationType.DISLIKE);
    }else{
      if(this.valoration.dislikedByUser){
        this.valoration.dislikedByUser = false;
        this.valoration.valoration += 1;
        this.likeAction.next(ValorationType.REMOVE);
      } else {
        this.valoration.dislikedByUser = true;
        this.valoration.likedByUser = false;
        this.valoration.valoration -= 1;
        this.likeAction.next(ValorationType.DISLIKE)
      }
    }


  }

  public like() {
    if(this.valoration.dislikedByUser){
      this.valoration.likedByUser = true;
      this.valoration.dislikedByUser = false;
      this.valoration.valoration += 2;
      this.likeAction.next(ValorationType.LIKE);
    }else {
      if(this.valoration.likedByUser){
        this.valoration.likedByUser = false;
        this.valoration.valoration -= 1;
        this.likeAction.next(ValorationType.REMOVE);
      } else {
        this.valoration.likedByUser = true;
        this.valoration.dislikedByUser = false;
        this.valoration.valoration += 1;
        this.likeAction.next(ValorationType.LIKE);
      }
    }

  }


  public async emitValorationEvent(action: ValorationType){

    switch (action) {
      case ValorationType.LIKE:
        this.likeEvent.emit();
        break;
      case ValorationType.DISLIKE:
        this.dislikeEvent.emit();
        break;
      default:
        this.removeEvent.emit();
        break;
    }



  }
}
