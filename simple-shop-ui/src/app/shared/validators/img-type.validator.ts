import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export function imgTypeValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const file: File | null = control.value;
    const allowedExtensions: string[] = ['jpg', 'jpeg', 'png'];

    if (file) {
      const fileName: string = file.name;
      const fileExtension = fileName.split('.').pop()?.toLowerCase();

      if (fileExtension && allowedExtensions.includes(fileExtension)) {
        return null;
      } else {
        return { invalidFileType: true };
      }
    }

    return null;
  };
}
