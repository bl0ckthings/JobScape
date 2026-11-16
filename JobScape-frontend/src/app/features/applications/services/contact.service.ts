import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Contact, ContactRequest } from '../../../shared/models/applications/contact.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ContactService {
  private readonly baseUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  createContact(contact: ContactRequest): Observable<Contact> {
    return this.http.post<Contact>(`${this.baseUrl}/api/contacts/create`, contact);
  }

  updateContact(id: number, contact: ContactRequest): Observable<Contact> {
    return this.http.put<Contact>(`${this.baseUrl}/api/contacts/update/${id}`, contact);
  }

  deleteContact(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/api/contacts/delete/${id}`);
  }
}
