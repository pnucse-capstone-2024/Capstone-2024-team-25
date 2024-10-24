/* eslint-disable no-shadow */
import { useEffect, useRef, useState } from 'react';
import { Link } from 'react-router-dom';

import { Button, ClickAwayListener, Grow, MenuItem, MenuList, Paper, Popper } from '@mui/material';

function HeaderTab({ title, links }) {
  const [open, setOpen] = useState(false);
  const anchorRef = useRef(null);

  const handleToggle = () => {
    setOpen((prevOpen) => !prevOpen);
  };

  const handleClose = (event) => {
    if (anchorRef.current && anchorRef.current.contains(event.target)) {
      return;
    }

    setOpen(false);
  };

  const handleListKeyDown = (event) => {
    if (event.key === 'Tab') {
      event.preventDefault();
      setOpen(false);
    } else if (event.key === 'Escape') {
      setOpen(false);
    }
  };

  // return focus to the button when we transitioned from !open -> open
  const prevOpen = useRef(open);
  useEffect(() => {
    if (prevOpen.current === true && open === false) {
      anchorRef.current.focus();
    }

    prevOpen.current = open;
  }, [open]);

  // 미디어 쿼리를 사용하여 화면 크기에 따라 placement 값을 설정
  const placement = window.innerWidth <= 1024 ? 'right-start' : 'bottom-start';

  return (
    <div className="break-keep">
      <Button
        ref={anchorRef}
        id="composition-button"
        aria-controls={open ? 'composition-menu' : undefined}
        aria-expanded={open ? 'true' : undefined}
        aria-haspopup="true"
        onClick={handleToggle}
        className="!hover:bg-gray-500 !px-4 !py-2 !text-lg !font-normal !text-gray-700"
      >
        {title}
      </Button>
      <Popper
        open={open}
        anchorEl={anchorRef.current}
        role={undefined}
        placement={placement}
        transition
        disablePortal
        anchororigin={{
          vertical: 'bottom',
          horizontal: 'right',
        }}
        transformorigin={{
          vertical: 'top',
          horizontal: 'right',
        }}
        style={{ width: 'auto', height: '200px' }}
        modifiers={[
          {
            name: 'offset',
            options: {
              offset: [0, 30], // x축으로 10px 오른쪽으로 이동
            },
          },
        ]}
        // className='border border-gray-200 rounded-md shadow-md bg-white'
      >
        {({ TransitionProps, placement }) => (
          <Grow
            // eslint-disable-next-line react/jsx-props-no-spreading
            {...TransitionProps}
            style={{
              transformOrigin: placement === 'bottom-start' ? 'left top' : 'left bottom',
            }}
          >
            <Paper>
              <ClickAwayListener onClickAway={handleClose}>
                <MenuList
                  className='!py-4 !px-1 !text-lg !font-normal !text-gray-700 shadow-md'
                  autoFocusItem={open}
                  id="composition-menu"
                  aria-labelledby="composition-button"
                  onKeyDown={handleListKeyDown}
                  style={{ padding: '8px' }}
                >
                  {links.map(({ name, link }) => (
                    <Link key={name} to={link}>
                      <MenuItem>{name}</MenuItem>
                    </Link>
                  ))}
                </MenuList>
              </ClickAwayListener>
            </Paper>
          </Grow>
        )}
      </Popper>
    </div>
  );
}

export default HeaderTab;