import {css} from 'emotion';

import {COLOR_WHITE} from '@/constants/colors';

export const breadCrumbWrapper = css`
  margin: 22px 0 0 22px;
`;

export const contentWrapper = css`
  min-height: calc(100vh - 64px);
`;

export const contentPageWrapper = css`
  margin: 16px 0 0 22px;
  padding: 16px;
  background-color: ${COLOR_WHITE};
`;

export const titlePage = css`
  font-size: 30px;
  margin: 0 0 16px 16px;
`;
