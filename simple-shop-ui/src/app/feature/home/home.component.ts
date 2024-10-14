import { Component, OnInit } from '@angular/core';
import { Stat } from '../../model/stat.model';
import { StatService } from '../../services/stat.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  isLoading: boolean = false;
  errorMessage: string = '';
  data: Stat | null = null;

  constructor(private statService: StatService) {}

  ngOnInit(): void {
      this.fetchStat()
  }

  fetchStat(): void {
    this.isLoading = true;
    this.statService.getState().subscribe({
      next: (response) => {
        if(response.status === 'OK'){
          this.data = response.data;
        } else {
          this.errorMessage = response.message
        }
        this.isLoading = false;
      },
      error: (error) => {
          this.errorMessage = 'Failed to load data.';
          this.isLoading = false;
      }
    })
  }
}
