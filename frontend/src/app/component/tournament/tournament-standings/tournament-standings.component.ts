import {Component, OnInit} from '@angular/core';
import {TournamentDetailParticipantDto, TournamentStandingsDto} from "../../../dto/tournament";
import {TournamentService} from "../../../service/tournament.service";
import {ActivatedRoute} from "@angular/router";
import {NgForm} from "@angular/forms";
import {Location} from "@angular/common";
import {ToastrService} from "ngx-toastr";
import {ErrorFormatterService} from "../../../service/error-formatter.service";

@Component({
  selector: 'app-tournament-standings',
  templateUrl: './tournament-standings.component.html',
  styleUrls: ['./tournament-standings.component.scss']
})
export class TournamentStandingsComponent implements OnInit {
  standings: TournamentStandingsDto | undefined;

  public constructor(
    private service: TournamentService,
    private errorFormatter: ErrorFormatterService,
    private route: ActivatedRoute,
    private notification: ToastrService,
    private location: Location,
  ) {
  }

  public ngOnInit() {
    // TODO to be implemented.
    this.route.params.subscribe((params) => {
    let id: number;
    id = params.id;
    this.service.getStandings(id).subscribe(
      (data: TournamentStandingsDto) => {
        this.standings = data;
        console.log(data)
      },
      (error : any) => {
        console.error('Error fetching standings:', error);
        // Handle error here, e.g., show an error message using ToastrService.
        this.notification.error('Error fetching standings', 'An error occurred while fetching standings.');
      }
    );
    });
  }

  public submit(form: NgForm) {
    // TODO to be implemented.
  }

  public generateFirstRound() {
    if (!this.standings)
      return;
    // TODO implement
  }
}
