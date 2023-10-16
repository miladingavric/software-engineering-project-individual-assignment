import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {map, Observable, throwError} from 'rxjs';
import {formatIsoDate} from '../util/date-helper';
import {
  TournamentCreateDto, TournamentDetailDto, TournamentDetailParticipantDto,
  TournamentListDto,
  TournamentSearchParams,
  TournamentStandingsDto, TournamentStandingsTreeDto
} from "../dto/tournament";
import {Horse} from "../dto/horse";
const baseUri = environment.backendUrl + '/tournaments';

class ErrorDto {
  constructor(public message: String) {}
}

@Injectable({
  providedIn: 'root'
})
export class TournamentService {
  constructor(
    private http: HttpClient
  ) {
  }

  // \TEMPLATE EXCLUDE END\
  public search(searchParams: TournamentSearchParams): Observable<TournamentListDto[]> {
    let params = new HttpParams();
    if (searchParams.name) {
      params = params.append('name', searchParams.name);
    }
    if (searchParams.startDate) {
      params = params.append('startDate', formatIsoDate(searchParams.startDate));
    }
    if (searchParams.endDate) {
      params = params.append('endDate', formatIsoDate(searchParams.endDate));
    }
    return this.http.get<TournamentListDto[]>(baseUri, { params });
  }
  // \TEMPLATE EXCLUDE END\

  public getStandings(id : number): Observable<TournamentStandingsDto> {
    return this.http.get<TournamentStandingsDto>(`${baseUri}/standings/${id}`);
  }

  public updateStandings(standings : TournamentStandingsDto | undefined): Observable<TournamentStandingsDto> {
    return this.http.put<TournamentStandingsDto>(`${baseUri}/standings/${standings?.id}`, standings);
  }

  public create(tournament: TournamentCreateDto): Observable<TournamentDetailDto> {
    // TODO this is not implemented yet!
    return this.http.post<TournamentDetailDto>(
      baseUri,
      tournament
    );
  }

  public generateFirstRoung(id : number): Observable<TournamentStandingsDto> {
    return this.http.get<TournamentStandingsDto>(`${baseUri}/standings/${id}`);
  }

}
