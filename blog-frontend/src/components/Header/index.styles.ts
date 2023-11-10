import {css} from 'emotion';

import {COLOR_DAVYS_GRAY, COLOR_GRAY, COLOR_WHITE} from '@/constants/colors';

export const headerContentWrapper = css`
  display: flex;
  justify-content: space-between;
`;

export const headerTitleWrapper = css`
  display: flex;
  flex-direction: row;
  margin: 5px 0px;
`;

export const styleTitle1 = css`
  padding-right: 7px;
  margin-top: 6px;
  width: 40px;
  height: 400px;  
`;

export const styleTitle2 = css`
  color: ${COLOR_DAVYS_GRAY};
  font-size: 14px;
  height: 20px;
`;

export const headerWrapper = css`
  height: 64px;
  background-color: ${COLOR_WHITE};
  border-bottom: 1px solid ${COLOR_GRAY};
`;
