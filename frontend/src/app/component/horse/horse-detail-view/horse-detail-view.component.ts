import { Component, Inject, LOCALE_ID } from '@angular/core';
import {Horse} from "../../../dto/horse";
import {Sex} from "../../../dto/sex";
import {HorseService} from "../../../service/horse.service";
import {BreedService} from "../../../service/breed.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {NgForm, NgModel} from "@angular/forms";
import {Breed} from "../../../dto/breed";
import {Observable, of} from "rxjs";
import {HorseCreateEditMode} from "../horse-create-edit/horse-create-edit.component";
import {formatDate} from "@angular/common";

@Component({
  selector: 'app-horse-detail-view',
  templateUrl: './horse-detail-view.component.html',
  styleUrls: ['./horse-detail-view.component.scss']
})
export class HorseDetailViewComponent {


  horse: Horse = {
    name: '',
    sex: Sex.female,
    dateOfBirth: new Date(), // TODO this is bad
    height: 0, // TODO this is bad
    weight: 0, // TODO this is bad
  };

  private heightSet: boolean = false;
  private weightSet: boolean = false;
  private dateOfBirthSet: boolean = false;

  get height(): number | null {
    return this.heightSet
      ? this.horse.height
      : null;
  }

  set height(value: number) {
    this.heightSet = true;
    this.horse.height = value;
  }

  get weight(): number | null {
    return this.weightSet
      ? this.horse.weight
      : null;
  }

  set weight(value: number) {
    this.weightSet = true;
    this.horse.weight = value;
  }

  get dateOfBirth(): Date | null {
    return this.dateOfBirthSet
      ? this.horse.dateOfBirth
      : null;
  }

  set dateOfBirth(value: Date) {
    this.dateOfBirthSet = true;
    this.horse.dateOfBirth = value;
  }

  formatThisDate(date : Date): String {
    return formatDate(date, "dd-MM-yyyy",  this.locale);
}


  constructor(
    private service: HorseService,
    private breedService: BreedService,
    private router: Router,
    private route: ActivatedRoute,
    private notification: ToastrService,
    @Inject(LOCALE_ID) public locale: string,
  ) {
  }




  get sex(): string {
    switch (this.horse.sex) {
      case Sex.male: return 'Male';
      case Sex.female: return 'Female';
      default: return '';
    }
  }


  ngOnInit(): void {
        this.route.params.subscribe((params) => {
          let horseId: number;
          horseId = params.id;
          this.service
            .getByID(horseId)
            .subscribe(
              (horse: Horse) => (
                (this.height = horse.height),
                  (this.weight = horse.weight),
                  (this.dateOfBirth = horse.dateOfBirth),
                  (this.horse = horse)
              )
            );
        });
  }

  editThisHorse(horse: Horse) {
    this.router.navigate([`horses/edit/${horse.id}`]);
  }

  public dynamicCssClassesForInput(input: NgModel): any {
    return {
      'is-invalid': !input.valid && !input.pristine,
    };
  }

  public formatBreedName(breed: Breed | null): string {
    return breed?.name ?? '';
  }

  breedSuggestions = (input: string) => (input === '')
    ? of([])
    :  this.breedService.breedsByName(input, 5);


  protected readonly formatDate = formatDate;
}
