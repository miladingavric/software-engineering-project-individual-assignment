import {Component, Inject, LOCALE_ID, OnInit} from '@angular/core';
import {ToastrService} from 'ngx-toastr';
import {debounceTime, Subject} from 'rxjs';
import {TournamentListDto, TournamentSearchParams} from "../../dto/tournament";
import {TournamentService} from "../../service/tournament.service";
import {formatDate} from "@angular/common";

@Component({
  selector: 'app-horse',
  templateUrl: './tournament.component.html',
  styleUrls: ['./tournament.component.scss']
})
export class TournamentComponent implements OnInit {
  search = false;
  tournaments: TournamentListDto[] = [];
  bannerError: string | null = null;
  searchParams: TournamentSearchParams = {};
  searchStartDate: string | null = null;
  searchEndDate: string | null = null;
  searchChangedObservable = new Subject<void>();

  constructor(
    private service: TournamentService,
    private notification: ToastrService,
    @Inject(LOCALE_ID) public locale: string,
  ) { }

  ngOnInit(): void {
    this.reloadTournaments();
    this.searchChangedObservable
      .pipe(debounceTime(300))
      .subscribe({next: () => this.reloadTournaments()});
  }


  reloadTournaments() {
    if (this.searchStartDate) {
      this.searchParams.startDate = new Date(this.searchStartDate);
    }
    if (this.searchEndDate) {
      this.searchParams.endDate = new Date(this.searchEndDate);
    }
    this.service.search(this.searchParams)
      .subscribe({
        next: data => {
          this.tournaments = data;
        },
        error: error => {
          console.error('Error fetching horses', error);
          this.bannerError = 'Could not fetch horses: ' + error.message;
          const errorMessage = error.status === 0
            ? 'Is the backend up?'
            : error.message.message;
          this.notification.error(errorMessage, 'Could Not Fetch Horses');
        }
      });
  }
  searchChanged(): void {
    this.searchChangedObservable.next();
  }

  toLocaleDateString(date : Date): String {
    return formatDate(date, "dd-MM-yyyy",  this.locale);
  }
}
