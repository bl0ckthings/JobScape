import { Component } from '@angular/core';
import {MatMenuModule} from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
@Component({
  selector: 'app-application-card',
  imports: [MatMenuModule, MatIconModule,MatButtonModule],
  templateUrl: './application-card.html',
  styleUrl: './application-card.css',
})
export class ApplicationCard {


}
