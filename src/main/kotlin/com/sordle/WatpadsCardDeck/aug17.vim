let SessionLoad = 1
let s:so_save = &g:so | let s:siso_save = &g:siso | setg so=0 siso=0 | setl so=-1 siso=-1
let v:this_session=expand("<sfile>:p")
silent only
silent tabonly
cd ~/Documents/coding/watpads-card-deck/src/main/kotlin/com/sordle/WatpadsCardDeck
if expand('%') == '' && !&modified && line('$') <= 1 && getline(1) == ''
  let s:wipebuf = bufnr('%')
endif
let s:shortmess_save = &shortmess
if &shortmess =~ 'A'
  set shortmess=aoOA
else
  set shortmess=aoO
endif
badd +9 model/Action.kt
badd +10 service/GameService.kt
badd +1 repository/GameRepository.kt
badd +43 controller/GameController.kt
badd +8 model/ClientState.kt
badd +20 model/GameStates.kt
badd +1 Game.kt
badd +1 Player.kt
argglobal
%argdel
$argadd model/Action.kt
edit Game.kt
let s:save_splitbelow = &splitbelow
let s:save_splitright = &splitright
set splitbelow splitright
wincmd _ | wincmd |
vsplit
1wincmd h
wincmd w
wincmd _ | wincmd |
split
1wincmd k
wincmd w
let &splitbelow = s:save_splitbelow
let &splitright = s:save_splitright
wincmd t
let s:save_winminheight = &winminheight
let s:save_winminwidth = &winminwidth
set winminheight=0
set winheight=1
set winminwidth=0
set winwidth=1
exe 'vert 1resize ' . ((&columns * 70 + 70) / 140)
exe '2resize ' . ((&lines * 20 + 21) / 42)
exe 'vert 2resize ' . ((&columns * 69 + 70) / 140)
exe '3resize ' . ((&lines * 19 + 21) / 42)
exe 'vert 3resize ' . ((&columns * 69 + 70) / 140)
argglobal
balt model/GameStates.kt
setlocal fdm=manual
setlocal fde=0
setlocal fmr={{{,}}}
setlocal fdi=#
setlocal fdl=0
setlocal fml=1
setlocal fdn=20
setlocal fen
silent! normal! zE
let &fdl = &fdl
let s:l = 34 - ((31 * winheight(0) + 20) / 40)
if s:l < 1 | let s:l = 1 | endif
keepjumps exe s:l
normal! zt
keepjumps 34
normal! 035|
wincmd w
argglobal
if bufexists(fnamemodify("Player.kt", ":p")) | buffer Player.kt | else | edit Player.kt | endif
if &buftype ==# 'terminal'
  silent file Player.kt
endif
balt service/GameService.kt
setlocal fdm=manual
setlocal fde=0
setlocal fmr={{{,}}}
setlocal fdi=#
setlocal fdl=0
setlocal fml=1
setlocal fdn=20
setlocal fen
silent! normal! zE
let &fdl = &fdl
let s:l = 7 - ((6 * winheight(0) + 10) / 20)
if s:l < 1 | let s:l = 1 | endif
keepjumps exe s:l
normal! zt
keepjumps 7
normal! 0
wincmd w
argglobal
if bufexists(fnamemodify("service/GameService.kt", ":p")) | buffer service/GameService.kt | else | edit service/GameService.kt | endif
if &buftype ==# 'terminal'
  silent file service/GameService.kt
endif
balt Player.kt
setlocal fdm=manual
setlocal fde=0
setlocal fmr={{{,}}}
setlocal fdi=#
setlocal fdl=0
setlocal fml=1
setlocal fdn=20
setlocal fen
silent! normal! zE
let &fdl = &fdl
let s:l = 19 - ((18 * winheight(0) + 9) / 19)
if s:l < 1 | let s:l = 1 | endif
keepjumps exe s:l
normal! zt
keepjumps 19
normal! 0
wincmd w
exe 'vert 1resize ' . ((&columns * 70 + 70) / 140)
exe '2resize ' . ((&lines * 20 + 21) / 42)
exe 'vert 2resize ' . ((&columns * 69 + 70) / 140)
exe '3resize ' . ((&lines * 19 + 21) / 42)
exe 'vert 3resize ' . ((&columns * 69 + 70) / 140)
tabnext 1
if exists('s:wipebuf') && len(win_findbuf(s:wipebuf)) == 0 && getbufvar(s:wipebuf, '&buftype') isnot# 'terminal'
  silent exe 'bwipe ' . s:wipebuf
endif
unlet! s:wipebuf
set winheight=1 winwidth=20
let &shortmess = s:shortmess_save
let &winminheight = s:save_winminheight
let &winminwidth = s:save_winminwidth
let s:sx = expand("<sfile>:p:r")."x.vim"
if filereadable(s:sx)
  exe "source " . fnameescape(s:sx)
endif
let &g:so = s:so_save | let &g:siso = s:siso_save
set hlsearch
doautoall SessionLoadPost
unlet SessionLoad
" vim: set ft=vim :
