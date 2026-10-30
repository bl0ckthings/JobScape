import { TestBed } from '@angular/core/testing';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ApplicationDetailDialog } from './application-detail-dialog';
import { createApplicationDetailDemo } from './application-detail.demo';

describe('ApplicationDetailDialog preview', () => {
  async function render(empty = false) {
    const data = createApplicationDetailDemo();
    if (empty) {
      data.steps = [];
      data.contacts = [];
      data.reminders = [];
    }
    await TestBed.configureTestingModule({
      imports: [ApplicationDetailDialog],
      providers: [{ provide: MAT_DIALOG_DATA, useValue: data }],
    }).compileComponents();
    const fixture = TestBed.createComponent(ApplicationDetailDialog);
    await fixture.whenStable();
    return fixture;
  }
  function click(root: HTMLElement, text: string) {
    const button = [...root.querySelectorAll('button')].find((item) =>
      item.textContent?.includes(text),
    );
    expect(button, `Button ${text}`).toBeTruthy();
    button!.click();
  }
  it('renders the Figma header and switches between all three tabs', async () => {
    const fixture = await render();
    const root = fixture.nativeElement as HTMLElement;
    expect(root.textContent).toContain('Dassault');
    expect(root.querySelector('[role="tabpanel"]')?.textContent).toContain('Entretien RH');
    click(root, 'Interlocuteurs');
    await fixture.whenStable();
    expect(root.querySelector('[role="tabpanel"]')?.textContent).toContain('Marie Blachère');
    click(root, 'Rappels');
    await fixture.whenStable();
    expect(root.querySelector('[role="tabpanel"]')?.textContent).toContain(
      'Relancer après entretien',
    );
  });
  it('opens and cancels every creation form without losing the list', async () => {
    const fixture = await render();
    const root = fixture.nativeElement as HTMLElement;
    for (const [tab, action, title] of [
      ['Étapes', 'Ajouter une étape', 'Nouvelle étape'],
      ['Interlocuteurs', 'Ajouter un interlocuteur', 'Nouvel interlocuteur'],
      ['Rappels', 'Ajouter un rappel', 'Nouveau rappel'],
    ]) {
      click(root, tab);
      await fixture.whenStable();
      click(root, action);
      await fixture.whenStable();
      expect(root.querySelector('form')?.textContent).toContain(title);
      click(root, 'Annuler');
      await fixture.whenStable();
      expect(root.querySelector('form')).toBeNull();
    }
  });
  it('edits a contact locally and leaves the supplied data untouched', async () => {
    const fixture = await render();
    const root = fixture.nativeElement as HTMLElement;
    click(root, 'Interlocuteurs');
    await fixture.whenStable();
    click(root, 'Marie Blachère');
    await fixture.whenStable();
    const input = root.querySelector<HTMLInputElement>('#detail-contact-name')!;
    input.value = 'Marie Dupont';
    input.dispatchEvent(new Event('input'));
    root
      .querySelector('form')!
      .dispatchEvent(new Event('submit', { bubbles: true, cancelable: true }));
    await fixture.whenStable();
    expect(root.querySelector('[role="tabpanel"]')?.textContent).toContain('Marie Dupont');
    expect(TestBed.inject(MAT_DIALOG_DATA).contacts[0].name).toBe('Marie Blachère');
  });
  it('supports arrow-key navigation across the tabs', async () => {
    const fixture = await render();
    const root = fixture.nativeElement as HTMLElement;
    root
      .querySelector('[role="tab"]')!
      .dispatchEvent(new KeyboardEvent('keydown', { key: 'ArrowRight', bubbles: true }));
    await fixture.whenStable();
    expect(root.querySelector('[role="tab"][aria-selected="true"]')?.textContent).toContain(
      'Interlocuteurs',
    );
  });
  it('validates a new contact before adding it to the preview', async () => {
    const fixture = await render();
    const root = fixture.nativeElement as HTMLElement;
    click(root, 'Interlocuteurs');
    await fixture.whenStable();
    click(root, 'Ajouter un interlocuteur');
    await fixture.whenStable();
    root
      .querySelector('form')!
      .dispatchEvent(new Event('submit', { bubbles: true, cancelable: true }));
    await fixture.whenStable();
    expect(root.textContent).toContain('Le nom est obligatoire.');
    expect(fixture.componentInstance.data().contacts).toHaveLength(2);
  });
  it('removes a preview contact and detaches it from the preview steps', async () => {
    const fixture = await render();
    const root = fixture.nativeElement as HTMLElement;
    click(root, 'Interlocuteurs');
    await fixture.whenStable();
    click(root, 'Marie Blachère');
    await fixture.whenStable();
    click(root, 'Supprimer');
    await fixture.whenStable();
    expect(fixture.componentInstance.data().contacts).toHaveLength(1);
    expect(fixture.componentInstance.data().steps[0].contactId).toBeNull();
    expect(TestBed.inject(MAT_DIALOG_DATA).contacts).toHaveLength(2);
  });
  it('uses a quick date and adds a reminder only to the preview', async () => {
    const fixture = await render();
    const root = fixture.nativeElement as HTMLElement;
    click(root, 'Rappels');
    await fixture.whenStable();
    click(root, 'Ajouter un rappel');
    await fixture.whenStable();
    click(root, 'Dans 3 jours');
    await fixture.whenStable();
    const date = root.querySelector<HTMLInputElement>('#detail-reminder-date')!.value;
    expect(date).toMatch(/^\d{4}-\d{2}-\d{2}$/);
    root
      .querySelector('form')!
      .dispatchEvent(new Event('submit', { bubbles: true, cancelable: true }));
    await fixture.whenStable();
    expect(fixture.componentInstance.data().reminders).toHaveLength(3);
    expect(fixture.componentInstance.data().reminders[2].date).toBe(date);
    expect(TestBed.inject(MAT_DIALOG_DATA).reminders).toHaveLength(2);
  });
  it('shows an empty state for every collection', async () => {
    const fixture = await render(true);
    const root = fixture.nativeElement as HTMLElement;
    for (const [tab, message] of [
      ['Étapes', 'Aucune étape'],
      ['Interlocuteurs', 'Aucun interlocuteur'],
      ['Rappels', 'Aucun rappel'],
    ]) {
      click(root, tab);
      await fixture.whenStable();
      expect(root.textContent).toContain(message);
    }
  });
});
