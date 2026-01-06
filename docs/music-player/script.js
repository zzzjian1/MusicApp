const tabs = document.querySelectorAll('.tab');
const pages = document.querySelectorAll('.page');
const tabbar = document.querySelector('.tabbar');
const indicator = document.querySelector('.tab-indicator');
tabs.forEach(t => t.addEventListener('click', () => {
  const target = t.dataset.target;
  tabs.forEach(x => x.classList.remove('active'));
  t.classList.add('active');
  pages.forEach(p => p.classList.toggle('active', p.id === target));
  updateTabIndicator();
}));

function openPlayer(title, artist, album) {
  document.querySelector('#song-title').textContent = title;
  document.querySelector('#song-sub').textContent = `${artist} · ${album}`;
  document.querySelector('#mini-title').textContent = title;
  document.querySelector('#mini-sub').textContent = `${artist} · ${album}`;
  document.querySelector('#mini-player').classList.add('show');
  document.querySelector('.tab[data-target="player"]').click();
}

let playing = false;
function togglePlay() {
  playing = !playing;
  document.querySelector('#play-btn').textContent = playing ? '⏸' : '▶';
  document.querySelector('#mini-toggle').textContent = playing ? '⏸' : '▶';
}
function next() {}
function prev() {}
function setMode(mode) { console.log('repeat mode:', mode); }

function toggleDark(enable) {
  document.documentElement.classList.toggle('dark', enable);
}

// Default to library
document.querySelector('.tab[data-target="library"]').click();

// Mini-player controls
document.querySelector('#mini-toggle').addEventListener('click', togglePlay);
document.querySelector('#mini-prev').addEventListener('click', prev);
document.querySelector('#mini-next').addEventListener('click', next);

// Live search filtering
const searchInput = document.querySelector('#library .search');
const cards = () => Array.from(document.querySelectorAll('#library .card'));
searchInput.addEventListener('input', (e) => {
  const q = e.target.value.trim().toLowerCase();
  cards().forEach(card => {
    const text = card.textContent.toLowerCase();
    card.style.display = text.includes(q) ? '' : 'none';
  });
});

// Segmented repeat control
const segments = document.querySelectorAll('#repeat-group .segment');
let repeatMode = 'none';
segments.forEach(seg => seg.addEventListener('click', () => {
  segments.forEach(s => s.classList.remove('active'));
  seg.classList.add('active');
  repeatMode = seg.dataset.mode;
  setMode(repeatMode);
}));

function updateTabIndicator() {
  if (!indicator) return;
  const active = document.querySelector('.tabbar .tab.active');
  indicator.style.display = 'block';
  const rectBar = tabbar.getBoundingClientRect();
  const rect = active.getBoundingClientRect();
  const left = rect.left - rectBar.left;
  indicator.style.width = rect.width + 'px';
  indicator.style.transform = `translateX(${left}px)`;
}

window.addEventListener('resize', updateTabIndicator);
updateTabIndicator();

// Playlists subtabs
const pTabs = document.querySelectorAll('#playlists-tabs .segment');
const pPages = {
  'playlists-list': document.getElementById('playlists-list'),
  'playlists-settings': document.getElementById('playlists-settings')
};
pTabs.forEach(t => t.addEventListener('click', () => {
  pTabs.forEach(x => x.classList.remove('active'));
  t.classList.add('active');
  const target = t.dataset.subpage;
  Object.keys(pPages).forEach(k => pPages[k].classList.toggle('active', k === target));
}));
